package mk.ukim.finki.synergymed.jobs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.ZoneId;

@Component
public class RevenueScheduledJobs {

    @PersistenceContext
    private EntityManager entityManager;

    // 09:05 1st every mont
    @Scheduled(cron = "0 5 9 1 * *")
    @Transactional
    public void callMonthlyRevenueForPreviousMonth() {
        ZoneId zone = ZoneId.systemDefault();
        YearMonth prev = YearMonth.now(zone).minusMonths(1);
        int year = prev.getYear();
        int month = prev.getMonthValue();

        entityManager
                .createNativeQuery("CALL synergymed.sp_monthly_pharmacy_revenue(:y, :m)")
                .setParameter("y", year)
                .setParameter("m", month)
                .executeUpdate();
    }

    // every day 8
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void callExpiringPrescriptions() {
        int daysAhead = 7;
        entityManager
                .createNativeQuery("CALL synergymed.sp_expiring_prescriptions(:d)")
                .setParameter("d", daysAhead)
                .executeUpdate();
    }
}
