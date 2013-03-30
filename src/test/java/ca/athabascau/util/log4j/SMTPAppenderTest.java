/**
 * This file is part of the au-log4j package; aka Athabasca University log4j
 * addons.
 * 
 * Copyright Trenton D. Adams <trenton daught d daught adams at gmail daught ca>
 * 
 * au-log4j is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * au-log4j is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with au-log4j.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * See the COPYING file for more information.
 */
package ca.athabascau.util.log4j;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jvnet.mock_javamail.Mailbox;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Created :  2012-06-21T09:55 MST
 *
 * @author trenta
 */

public class SMTPAppenderTest extends TestCase
{
    private static final Logger logger = Logger.getLogger(
        SMTPAppender.class.getName());
    private Session session;
    private Store store;
    private Folder folder;

    protected void setUp() throws Exception
    {
        super.setUp();
        session = Session.getInstance(new Properties());
        session.setDebug(false);
        store = session.getStore("pop3");
        store.connect("athabascau.ca", "waas-sysmsg", "anything");
        folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
    }

    public void testError() throws MessagingException, IOException
    {
        logger.error("This is an error");
        Assert.assertFalse("message not found when it should have been",
            checkMail("(?s)(?i).*error log test.*This is an error.*"));
    }

    public void testMatch() throws MessagingException, IOException
    {
        logger.error("ErrorCommand logging: student id - null\n" +
            "ca.montage.banner.exception.BannerException: an unknown error occurred\n" +
            "\tat ca.montage.banner.web.DispatcherServlet.doGet(DispatcherServlet.java:217)\n" +
            "\tat ca.montage.banner.web.DispatcherServlet.doPost(DispatcherServlet.java:321)\n" +
            "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:709)\n" +
            "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:802)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:252)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:173)\n" +
            "\tat ca.athabascau.input.InputSanityFilter.doFilter(InputSanityFilter.java:102)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:202)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:173)\n" +
            "\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:214)\n" +
            "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:104)\n" +
            "\tat org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:520)\n" +
            "\tat org.apache.catalina.core.StandardContextValve.invokeInternal(StandardContextValve.java:198)\n" +
            "\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:144)\n" +
            "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:104)\n" +
            "\tat org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:520)\n" +
            "\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:137)\n" +
            "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:104)\n" +
            "\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:118)\n" +
            "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:102)\n" +
            "\tat org.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:535)\n" +
            "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:102)\n" +
            "\tat org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:520)\n" +
            "\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:109)\n" +
            "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:104)\n" +
            "\tat org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:520)\n" +
            "\tat org.apache.catalina.core.ContainerBase.invoke(ContainerBase.java:929)\n" +
            "\tat org.apache.coyote.tomcat5.CoyoteAdapter.service(CoyoteAdapter.java:160)\n" +
            "\tat org.apache.jk.server.JkCoyoteHandler.invoke(JkCoyoteHandler.java:300)\n" +
            "\tat org.apache.jk.common.HandlerRequest.invoke(HandlerRequest.java:374)\n" +
            "\tat org.apache.jk.common.ChannelSocket.invoke(ChannelSocket.java:743)\n" +
            "\tat org.apache.jk.common.ChannelSocket.processConnection(ChannelSocket.java:675)\n" +
            "\tat org.apache.jk.common.SocketConnection.runIt(ChannelSocket.java:866)\n" +
            "\tat org.apache.tomcat.util.threads.ThreadPool$ControlRunnable.run(ThreadPool.java:684)\n" +
            "\tat java.lang.Thread.run(Thread.java:534)\n" +
            "Caused by: java.lang.NullPointerException\n" +
            "\tat ca.montage.banner.web.commands.admission.UGAdmissionCommand.logParameters(UGAdmissionCommand.java:1376)\n" +
            "\tat ca.montage.banner.web.commands.admission.UGAdmissionCommand.execute(UGAdmissionCommand.java:927)\n" +
            "\tat ca.montage.banner.web.DispatcherServlet.executeCommand(DispatcherServlet.java:290)\n" +
            "\tat ca.montage.banner.web.DispatcherServlet.doGet(DispatcherServlet.java:111)\n" +
            "\t... 34 more\n");
        Assert.assertTrue("\"Existing bug #1810...\" message not found when " +
            "it should have been",
            checkMail("(?s)(?i).*Existing bug #1810.*student id - " +
                "null.*an unknown error occurred.*NullPointerException.*"));
    }

    public void testFloodProtection() throws IOException, MessagingException
    {
        for (int index = 0; index < 10; index++)
        {
            logger.error("ErrorCommand logging: student id - null\n" +
                "ca.montage.banner.exception.BannerException: an unknown error occurred\n" +
                "\tat ca.montage.banner.web.DispatcherServlet.doGet(DispatcherServlet.java:217)\n" +
                "\tat ca.montage.banner.web.DispatcherServlet.doPost(DispatcherServlet.java:321)\n" +
                "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:709)\n" +
                "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:802)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:252)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:173)\n" +
                "\tat ca.athabascau.input.InputSanityFilter.doFilter(InputSanityFilter.java:102)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:202)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:173)\n" +
                "\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:214)\n" +
                "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:104)\n" +
                "\tat org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:520)\n" +
                "\tat org.apache.catalina.core.StandardContextValve.invokeInternal(StandardContextValve.java:198)\n" +
                "\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:144)\n" +
                "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:104)\n" +
                "\tat org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:520)\n" +
                "\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:137)\n" +
                "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:104)\n" +
                "\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:118)\n" +
                "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:102)\n" +
                "\tat org.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:535)\n" +
                "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:102)\n" +
                "\tat org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:520)\n" +
                "\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:109)\n" +
                "\tat org.apache.catalina.core.StandardValveContext.invokeNext(StandardValveContext.java:104)\n" +
                "\tat org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:520)\n" +
                "\tat org.apache.catalina.core.ContainerBase.invoke(ContainerBase.java:929)\n" +
                "\tat org.apache.coyote.tomcat5.CoyoteAdapter.service(CoyoteAdapter.java:160)\n" +
                "\tat org.apache.jk.server.JkCoyoteHandler.invoke(JkCoyoteHandler.java:300)\n" +
                "\tat org.apache.jk.common.HandlerRequest.invoke(HandlerRequest.java:374)\n" +
                "\tat org.apache.jk.common.ChannelSocket.invoke(ChannelSocket.java:743)\n" +
                "\tat org.apache.jk.common.ChannelSocket.processConnection(ChannelSocket.java:675)\n" +
                "\tat org.apache.jk.common.SocketConnection.runIt(ChannelSocket.java:866)\n" +
                "\tat org.apache.tomcat.util.threads.ThreadPool$ControlRunnable.run(ThreadPool.java:684)\n" +
                "\tat java.lang.Thread.run(Thread.java:534)\n" +
                "Caused by: java.lang.NullPointerException\n" +
                "\tat ca.montage.banner.web.commands.admission.UGAdmissionCommand.logParameters(UGAdmissionCommand.java:1376)\n" +
                "\tat ca.montage.banner.web.commands.admission.UGAdmissionCommand.execute(UGAdmissionCommand.java:927)\n" +
                "\tat ca.montage.banner.web.DispatcherServlet.executeCommand(DispatcherServlet.java:290)\n" +
                "\tat ca.montage.banner.web.DispatcherServlet.doGet(DispatcherServlet.java:111)\n" +
                "\t... 34 more\n");
        }
        Assert.assertTrue("\"Existing bug #1810...\" message not found when " +
            "it should have been",
            checkMail("(?s)(?i).*Existing bug #1810.*student id - " +
                "null.*an unknown error occurred.*NullPointerException.*"));
        Assert.assertTrue("flood protection message not found",
            checkMail("(?s)(?i).*Flood protection enabled.*1000ms.*"));
        final Message[] messages = folder.getMessages();
        Assert.assertTrue("There should only be four messages, as the " +
            "rest should have hit the flood limit", messages.length == 4);
    }


    /**
     * Checks the mail box to determine if a message that looks like the one
     * specified exists.
     *
     * @param bodyPattern the java compatible regex pattern to match.  This
     *                    may include anything in the original source of the
     *                    email message.
     *
     * @return true if the message exists, false otherwise.
     *
     * @throws MessagingException
     * @throws IOException
     */
    public boolean checkMail(final String bodyPattern)
        throws MessagingException, IOException
    {
        final Message[] messages = folder.getMessages();

        System.out.println("INBOX has " + messages.length + " messages");
        for (int index = 0; index < messages.length; index++)
        {
            final InputStream inputStream = messages[index].getInputStream();
            final String emailSource = IOUtils.toString(inputStream, "UTF-8");
            if (emailSource.matches(bodyPattern))
            {   // email source matches the regex provided
                return true;
            }
        }

        return false;
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        Mailbox.clearAll();
    }
}
