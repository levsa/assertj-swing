package org.assertj.swing.launcher;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.SwingUtilities;

import netx.jnlp.LaunchException;
import netx.jnlp.LaunchHandler;
import netx.jnlp.Launcher;
import netx.jnlp.event.ApplicationEvent;
import netx.jnlp.event.ApplicationListener;
import netx.jnlp.runtime.ApplicationInstance;
import netx.jnlp.runtime.JNLPRuntime;


public class NetxJnlpLauncher implements JnlpLauncher
{
    private URL url;

    public static class NetxLaunchHandler implements LaunchHandler
    {
        public void launchCompleted(ApplicationInstance application) {
            System.out.println("COMPLETED: " + application.getTitle());
        }
        public void launchError(LaunchException error) {
            System.out.println("ERROR: " + error.getMessage());
        }
        public boolean launchWarning(LaunchException warning) {
            System.out.println("WARNING: " + warning.getMessage());
            return true;
        }
        public boolean validationError(LaunchException security) {
            System.out.println("VALIDATIONERROR: " + security.getMessage());
            return true;
        }
    };

    private LaunchHandler launchHandler = new NetxLaunchHandler();

    protected void setUrl(String url)
    {
        try
        {
            this.url = new URL(url);
        } catch (MalformedURLException e)
        {
            throw new RuntimeException("Malformed url '" + url +"'", e);
        }
    }

    public JnlpLauncher atUrl(String url)
    {
        setUrl(url);
        return this;
    }

    public JnlpLauncher start()
    {
        try
        {
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    Launcher launcher = new Launcher(launchHandler);
                    JNLPRuntime.setBaseDir(new File("."));
                    JNLPRuntime.setSecurityEnabled(false);
                    JNLPRuntime.initialize();
//                    try
//                    {
                        launcher.launchBackground(url);
//                        launcher.launch(url);
//                    }
//                    catch (LaunchException e)
//                    {
//                        e.printStackTrace();
//                    }

                }
            };
            SwingUtilities.invokeLater(runnable);
            return this;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error reading jnlp file at '" + url +
                "'.  Please test launch the URL in your browser.", e);
        }
    }
}
