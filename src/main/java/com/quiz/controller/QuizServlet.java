package com.quiz.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        Integer index = (Integer) session.getAttribute("index");

        // -------- START PAGE --------
        if (index == null) {

            out.println("<html><head><meta charset='UTF-8'>");
            out.println("<style>");
            out.println("body{background:black;color:white;text-align:center;font-family:sans-serif;}");
            out.println(".box{border:2px solid red;padding:30px;width:350px;margin:auto;margin-top:100px;box-shadow:0 0 20px red;}");
            out.println("input,select{width:90%;padding:8px;margin:10px 0;background:black;color:white;border:1px solid red;}");
            out.println("button{background:red;color:white;padding:10px 20px;border:none;cursor:pointer;}");
            out.println("</style></head>");

            out.println("<body>");
            out.println("<h1 style='color:red;'>🔥 Rapid Fire Round 🔥</h1>");

            out.println("<div class='box'>");
            out.println("<form action='quiz' method='post'>");

            out.println("Player 1:<br><input name='p1' required><br>");
            out.println("<div id='p2div'>Player 2:<br><input name='p2'><br></div>");

            out.println("Mode:<br>");
            out.println("<select name='mode' id='mode'>");
            out.println("<option value='multi'>Multiplayer</option>");
            out.println("<option value='computer'>Computer</option>");
            out.println("</select>");

            out.println("Difficulty:<br>");
            out.println("<select name='level'>");
            out.println("<option value='1'>Easy</option>");
            out.println("<option value='2'>Medium</option>");
            out.println("<option value='3'>Hard</option>");
            out.println("</select><br><br>");

            out.println("<button type='submit'>Start Quiz</button>");
            out.println("</form></div>");

            // hide p2
            out.println("<script>");
            out.println("const mode=document.getElementById('mode');");
            out.println("const p2=document.getElementById('p2div');");
            out.println("function toggle(){p2.style.display=(mode.value==='computer')?'none':'block';}");
            out.println("mode.addEventListener('change',toggle);toggle();");
            out.println("</script>");

            out.println("</body></html>");
            return;
        }

        List<String> questions = (List<String>) session.getAttribute("questions");

        // -------- QUESTION PAGE --------
        if (index < questions.size()) {

            out.println("<html><head><meta charset='UTF-8'>");
            out.println("<style>");
            out.println("body{background:black;color:white;text-align:center;font-family:sans-serif;}");
            out.println(".box{border:2px solid red;padding:30px;width:400px;margin:auto;margin-top:100px;box-shadow:0 0 20px red;}");
            out.println("input{width:90%;padding:8px;margin:10px 0;background:black;color:white;border:1px solid red;}");
            out.println("button{background:red;color:white;padding:10px 20px;border:none;cursor:pointer;}");
            out.println("</style></head>");

            out.println("<body>");
            out.println("<h2 style='color:red;'>Question " + (index + 1) + " of " + questions.size() + "</h2>");
            out.println("<h2>Timer: <span id='timer' style='color:red;'>10</span>s</h2>");

            out.println("<div class='box'>");

            // ✅ Bigger question text
            out.println("<h1 style='font-size:28px;'>" + questions.get(index) + "</h1>");

            out.println("<form id='form' action='quiz' method='post'>");

            out.println("Player 1:<br><input name='a1'><br>");

            String mode = (String) session.getAttribute("mode");
            if ("multi".equals(mode)) {
                out.println("Player 2:<br><input name='a2'><br>");
            }

            out.println("<button type='submit'>Submit</button>");
            out.println("</form></div>");

            // timer
            out.println("<script>");
            out.println("let t=10;");
            out.println("let timer=setInterval(()=>{");
            out.println("document.getElementById('timer').innerText=t;");
            out.println("t--;");
            out.println("if(t<0){clearInterval(timer);document.getElementById('form').submit();}");
            out.println("},1000);");
            out.println("</script>");

            out.println("</body></html>");
        }

        // -------- RESULT PAGE --------
        else {

            int s1 = (Integer) session.getAttribute("score1");
            int s2 = (Integer) session.getAttribute("score2");

            String p1 = (String) session.getAttribute("p1");
            String p2 = (String) session.getAttribute("p2");

            out.println("<html><head><meta charset='UTF-8'>");
            out.println("<style>");
            out.println("body{background:black;color:white;text-align:center;font-family:sans-serif;}");
            out.println(".box{border:2px solid red;padding:30px;width:350px;margin:auto;margin-top:100px;box-shadow:0 0 20px red;}");
            out.println("</style></head>");

            out.println("<body>");
            out.println("<h1 style='color:red;'>🔥 GAME OVER 🔥</h1>");

            out.println("<div class='box'>");
            out.println("<h2>" + p1 + ": " + s1 + "</h2>");
            out.println("<h2>" + p2 + ": " + s2 + "</h2>");

            if (s1 > s2)
                out.println("<h2>🏆 Winner: " + p1 + "</h2>");
            else if (s2 > s1)
                out.println("<h2>🏆 Winner: " + p2 + "</h2>");
            else
                out.println("<h2>🤝 It's a Tie!</h2>");

            out.println("</div></body></html>");

            session.invalidate();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer index = (Integer) session.getAttribute("index");

        if (index == null) {

            String p1 = request.getParameter("p1");
            String p2 = request.getParameter("p2");
            String mode = request.getParameter("mode");

            if (mode == null || mode.trim().isEmpty()) {
                mode = "multi";
            }

            if (mode.equals("computer"))
                p2 = "Computer";

            int level = 1;
            String levelStr = request.getParameter("level");
            if (levelStr != null && !levelStr.trim().isEmpty()) {
                try { level = Integer.parseInt(levelStr); } catch (Exception e) { level = 1; }
            }

            List<String> q = new ArrayList<>();
            List<Integer> a = new ArrayList<>();

            if (level == 1) {
                q = Arrays.asList("5+3=?", "9+7=?", "12-5=?", "4*3=?", "20/4=?");
                a = Arrays.asList(8, 16, 7, 12, 5);
            } else if (level == 2) {
                q = Arrays.asList("5+5*4=?", "12*3-6=?", "50/5+7=?", "18+6*2=?", "30-4*5=?");
                a = Arrays.asList(25, 30, 17, 30, 10);
            } else {
                q = Arrays.asList("25*4-10=?", "36/3+15*2=?", "125/5+20=?", "(15+5)*3=?", "50-10*3+20=?");
                a = Arrays.asList(90, 42, 45, 60, 40);
            }

            session.setAttribute("questions", q);
            session.setAttribute("answers", a);
            session.setAttribute("index", 0);
            session.setAttribute("score1", 0);
            session.setAttribute("score2", 0);
            session.setAttribute("p1", p1);
            session.setAttribute("p2", p2);
            session.setAttribute("mode", mode);

            response.sendRedirect("quiz");
            return;
        }

        int qIndex = (Integer) session.getAttribute("index");
        int score1 = (Integer) session.getAttribute("score1");
        int score2 = (Integer) session.getAttribute("score2");

        List<Integer> answers = (List<Integer>) session.getAttribute("answers");
        int correct = answers.get(qIndex);

        String a1Str = request.getParameter("a1");
        int a1 = -1;
        if (a1Str != null && !a1Str.trim().isEmpty()) {
            try { a1 = Integer.parseInt(a1Str); } catch (Exception e) {}
        }

        String mode = (String) session.getAttribute("mode");

        if (mode.equals("multi")) {

            String a2Str = request.getParameter("a2");
            int a2 = -1;
            if (a2Str != null && !a2Str.trim().isEmpty()) {
                try { a2 = Integer.parseInt(a2Str); } catch (Exception e) {}
            }

            if (a1 == correct) score1++;
            if (a2 == correct) score2++;

        } else {

            Random r = new Random();
            int comp = r.nextInt(100) < 70 ? correct : correct + r.nextInt(5) - 2;

            if (a1 == correct) score1++;
            if (comp == correct) score2++;
        }

        session.setAttribute("index", qIndex + 1);
        session.setAttribute("score1", score1);
        session.setAttribute("score2", score2);

        response.sendRedirect("quiz");
    }
}