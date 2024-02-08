package maxim.butenko.weather.listener;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.dao.SessionDAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@WebListener
public class SessionExpirationListener implements ServletContextListener {
    private final SessionDAO sessionDAO = SessionDAO.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Timer timer = new Timer();
        SessionRemover sessionRemover = new SessionRemover();
        long expireTime = 24 * 60 * 60 * 1000;
        timer.schedule(sessionRemover, Calendar.getInstance().getTime(), expireTime);
    }

    private class SessionRemover extends TimerTask {
        @Override
        public void run() {
            sessionDAO.deleteByExpiredTime(LocalDateTime.now());
        }
    }
}

