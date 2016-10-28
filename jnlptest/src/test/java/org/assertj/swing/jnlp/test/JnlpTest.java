package org.assertj.swing.jnlp.test;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.ComponentFinder;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.launcher.JnlpLauncher;
import org.assertj.swing.launcher.Launcher;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import netx.jnlp.LaunchException;
import netx.jnlp.ParseException;

public class JnlpTest
{

    private final class IndexedTypeMatcher<T extends Component> extends GenericTypeMatcher<T>
    {
        private final int mIndex;
        private int foundIndex = 0;

        private IndexedTypeMatcher(Class<T> supportedType, int index)
        {
            super(supportedType);
            mIndex = index;
        }

        @Override
        protected boolean isMatching(T component)
        {
            System.out.println("TEXTBOX: " + component.getName() + ", index = " + foundIndex);
            if (foundIndex == mIndex)
            {
                foundIndex++;
                return true;
            }
            foundIndex++;
            return false;
        }
    }

    private final class ButtonTextMatcher extends GenericTypeMatcher<JButton>
    {
        private final String expectedText;

        private ButtonTextMatcher(String text)
        {
            super(JButton.class);
            expectedText = text;
        }

        @Override
        protected boolean isMatching(JButton button)
        {
            System.out.println("COMPONENT: " + button.getName() + ", text = " + button.getText());
            return expectedText.equals(button.getText());
        }
    }

    public static String JNLP_URL = "http://speed:1060/webstart/jnlp/iipax.jnlp";
    private static Robot mRobot;

    @BeforeClass
    public static void setUpClass() throws InterruptedException, MalformedURLException,
        LaunchException, IOException, ParseException
    {
        mRobot = BasicRobot.robotWithNewAwtHierarchyWithoutScreenLock();
        JnlpLauncher launcher = Launcher.jnlp();
//        String url = "https://docs.oracle.com/javase/tutorialJWS/samples/uiswing/InputVerificationDialogDemoProject/InputVerificationDialogDemo.jnlp";
//        String url = "https://docs.oracle.com/javase/tutorialJWS/samples/uiswing/ModalityDemoProject/ModalityDemo.jnlp";
        //URL url = new File("/Users/levsa/Downloads/InputVerificationDemo.jnlp").toURI().toURL();
        URL url = new File("/Users/levsa/Downloads/one_106_rc2.jnlp").toURI().toURL();
        launcher.atUrl(url.toString());
        launcher.start();
            //new JNLPFile(new File("/Users/levsa/Downloads/one_106_rc2.jnlp").toURI().toURL()));
//            new JNLPFile();
    }

    @Test @Ignore
    public void inputVerificationDemo() throws InterruptedException
    {
        dumpHierarchy();
        Thread.sleep(300 * 10);
        dumpHierarchy();
        ComponentFinder finder = robot().finder();
        Component component = finder.findByLabel("Loan Amount (10,000 - 10,000,000): ");
        JTextComponent textComponent = (JTextField) component;
        JTextComponentFixture fixture = new JTextComponentFixture(robot(), textComponent);
        fixture.requireVisible();
        fixture.setText("Hello ");
        Thread.sleep(300 * 10);
    }

    @Test
    public void application_launching_without_arguments_example() throws InterruptedException
    {
        login();
        dumpHierarchy();
        Thread.sleep(300 * 1000);
        FrameFixture frameFixture = findFrame();
        JPanelFixture panel = frameFixture.panel(new GenericTypeMatcher<JPanel>(JPanel.class)
        {
            @Override
            protected boolean isMatching(JPanel component)
            {
                System.out.println("PANEL: " + component.getName());
                if ("null.contentPane".equals(component.getName()))
                {
                    return true;
                }
                return false;
            }
        });
        System.out.println("PANEL FOUND");
        dumpHierarchy();
        panel.menuItem(new GenericTypeMatcher<JMenu>(JMenu.class)
        {
            @Override
            protected boolean isMatching(JMenu component)
            {
                System.out.println("MENU: " + component.getName() + ", " + component.getText());
                return false;
            }
        });
    }

    private void dumpHierarchy()
    {
        Collection<Container> roots = robot().hierarchy().roots();
        for (Container container : roots)
        {
            System.out.println("ROOT: " + container.getName() + ", class=" +
                container.getClass().getCanonicalName());
            dumpContainer(container, 1);
        }
    }

    private void dumpContainer(Container container, int indent)
    {
        String indentString = "";
        for (int i=0; i<indent; i++)
        {
            indentString += "  ";
        }
        for (int i=0; i < container.getComponentCount(); i++)
        {
            Component component = container.getComponent(i);
            System.out.println(indentString + "COMPONENT: " + component.getName() + ", class=" +
                component.getClass().getCanonicalName());
            if (component instanceof Container)
            {
                dumpContainer((Container) component, indent + 1);
            }
        }
    }

    private void login()
    {
        DialogFixture dialog = WindowFinder.findDialog(new GenericTypeMatcher<Dialog>(Dialog.class)
        {
            @Override
            protected boolean isMatching(Dialog dialog)
            {
                System.out.println("Found dialog: " + dialog.getTitle());
                return "Inloggning".equals(dialog.getTitle()) && dialog.isShowing();
            }
        }).using(robot());
        JTextComponentFixture username = dialog.textBox(indexMatcher(JTextComponent.class, 0));
        JTextComponentFixture password = dialog.textBox(indexMatcher(JTextComponent.class, 1));
        username.enterText("iipaxAdm");
        password.enterText("iipax123");
        dialog.button(buttonLabelMatcher("Logga in")).click();
    }

    private <T extends Component> GenericTypeMatcher<T> indexMatcher(Class<T> clazz, int index)
    {
        return new IndexedTypeMatcher<T>(clazz, index);
    }

    private GenericTypeMatcher<JButton> buttonLabelMatcher(String text)
    {
        return new ButtonTextMatcher(text);
    }

    private FrameFixture findFrame()
    {
        return WindowFinder.findFrame(new GenericTypeMatcher<Frame>(Frame.class)
        {
            protected boolean isMatching(Frame frame)
            {
                System.out.println("Found frame: " + frame.getTitle());
                return "frame2".equals(frame.getName()) && frame.isShowing();
            }
        }).using(robot());
    }

    private Robot robot()
    {
        return mRobot;
    }

}