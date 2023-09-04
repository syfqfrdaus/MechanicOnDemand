package psm.mechanicondemand.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityUserTroubleshootBinding;

public class UserTroubleshoot extends DrawerUser {

    ActivityUserTroubleshootBinding activityUserTroubleshootBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserTroubleshootBinding = activityUserTroubleshootBinding.inflate(getLayoutInflater());
        setContentView(activityUserTroubleshootBinding.getRoot());

        WebView webView = findViewById(R.id.tirechange);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/89rghWSBFgE?si=P-CYQKuvINSTTDiS\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        WebView webView2 = findViewById(R.id.basicmaintenance);
        String video2 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Y3jcQCdeJAs?si=zBCSUAHn8VEPGBWi\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView2.loadData(video2, "text/html","utf-8");
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.setWebChromeClient(new WebChromeClient());

        WebView webView3 = findViewById(R.id.caroverheat);
        String video3 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/iYD6G9tcFQA?si=UW4Ih0WzGKKoTNE-\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView3.loadData(video3, "text/html","utf-8");
        webView3.getSettings().setJavaScriptEnabled(true);
        webView3.setWebChromeClient(new WebChromeClient());

        WebView webView4 = findViewById(R.id.carwontstart);
        String video4 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/lz611bF6AYc?si=wQYO57JLaoKbvXP1\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView4.loadData(video4, "text/html","utf-8");
        webView4.getSettings().setJavaScriptEnabled(true);
        webView4.setWebChromeClient(new WebChromeClient());

        WebView webView5 = findViewById(R.id.soundfromtire);
        String video5 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ZrWfYh_9TZo?si=o1Vlntmz9pBVjD8D\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView5.loadData(video5, "text/html","utf-8");
        webView5.getSettings().setJavaScriptEnabled(true);
        webView5.setWebChromeClient(new WebChromeClient());



    }
}