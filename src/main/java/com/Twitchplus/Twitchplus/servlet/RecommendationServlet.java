package com.Twitchplus.Twitchplus.servlet;

import com.Twitchplus.Twitchplus.entity.Item;
import com.Twitchplus.Twitchplus.recommendation.ItemRecommender;
import com.Twitchplus.Twitchplus.recommendation.RecommendationException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RecommendationServlet", urlPatterns = {"/recommendation"})
public class RecommendationServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        ItemRecommender itemRecommender = new ItemRecommender();
        Map<String, List<Item>> itemMap;

        // If the user is successfully logged in, recommend by the favorite records, otherwise recommend by the top games.
        try {
            if (session == null) {
                itemMap = itemRecommender.recommendItemsByDefault();
            } else {
                String userId = (String) request.getSession().getAttribute("user_id");
                itemMap = itemRecommender.recommendItemsByUser(userId);
            }
        } catch (RecommendationException e) {
            throw new ServletException(e);
        }

        ServletUtil.writeItemMap(response, itemMap);
    }
}
