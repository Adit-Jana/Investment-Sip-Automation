package com.adit.groww_sip_tracker;

import com.adit.groww_sip_tracker.auth.GrowwAuthenticator;
import com.adit.groww_sip_tracker.broker.ApiClient;
import com.adit.groww_sip_tracker.broker.groww.GrowwApiAdapter;
import com.adit.groww_sip_tracker.broker.icici.BreezeApiAdapter;
import com.adit.groww_sip_tracker.config.ConfigLoader;
import com.adit.groww_sip_tracker.data.NavFetcher;
import com.adit.groww_sip_tracker.logic.ThresholdAnalyzer;
import com.adit.groww_sip_tracker.order.EmailNotifier;
import com.adit.groww_sip_tracker.order.GrowwOrderPlacer;
import com.adit.groww_sip_tracker.order.Notifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

//@SpringBootApplication
public class GrowwSipTrackerApplication {

	public static void main(String[] args) throws Exception {
		//SpringApplication.run(GrowwSipTrackerApplication.class, args);

		ApiClient api = switch (ConfigLoader.get("API_PROVIDER")) {
			case "groww"  -> new GrowwApiAdapter();
			case "breeze" -> new BreezeApiAdapter();
			//case "alpha"  -> new AlphaVantageAdapter();
			//case "mock"   -> new MockApiAdapter();
			default       -> throw new IllegalArgumentException("Unknown API provider");
		};


		double currentNav = NavFetcher.getLatestNAV();
		List<Double> history = Arrays.asList(205.0, 206.2, 204.5, 208.1, 207.0); // Simulate for now
		double dipPercent = 3.0; // Can pull from config

		/*if (ThresholdAnalyzer.shouldBuy(currentNav, history, dipPercent)) {
			GrowwOrderPlacer.placeOrder(1000); // Amount in rupees
		} else {
			System.out.println("ℹ️ NAV not low enough. Waiting for a better entry.");
		}*/

		boolean dryRun = Boolean.parseBoolean(ConfigLoader.get("DRY_RUN"));

		if (dryRun) {
			System.out.println("🧪 [DRY RUN] Order would have been placed at NAV ₹" + currentNav);
		} else {
			String accessToken = GrowwAuthenticator.getAccessToken();
			GrowwOrderPlacer.placeOrder(1000, accessToken, ConfigLoader.get("GROWW_FUND_ID"));
		}

		/*Notifier.sendTelegramMessage("✅ SIP order placed at NAV ₹" + currentNav, "<BOT_TOKEN>", "<CHAT_ID>");

		String subject = "✅ SIP Order Placed";
		String body = "Order executed at NAV ₹" + currentNav;
		EmailNotifier.sendEmail(subject, body,
				ConfigLoader.get("EMAIL_TO"),
				ConfigLoader.get("EMAIL_FROM"),
				ConfigLoader.get("EMAIL_PASSWORD"));*/

		if (!dryRun) {
			Notifier.sendTelegramMessage("✅ SIP order placed at NAV ₹" + currentNav, "<BOT_TOKEN>", "<CHAT_ID>");
			String subject = "✅ SIP Order Placed";
			String body = "Order executed at NAV ₹" + currentNav;
			EmailNotifier.sendEmail(subject, body,
					ConfigLoader.get("EMAIL_TO"),
					ConfigLoader.get("EMAIL_FROM"),
					ConfigLoader.get("EMAIL_PASSWORD"));
		} else {
			System.out.println("🔕 [DRY RUN] No alerts sent (dry run mode).");
		}
	}

}
