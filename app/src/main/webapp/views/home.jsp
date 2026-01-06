<%@ page contentType="text/html;charset=UTF-8" import="java.util.List" language="java" isELIgnored="false" %>

<section class="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-blue-100 to-white">
    <div class="text-center mb-10">
        <h1 class="text-4xl font-bold text-blue-700 mb-4">Bienvenue sur Carshare !</h1>
        <p class="text-lg text-gray-600">Trouvez ou proposez un trajet simplement.</p>

        <%  
            String error = (String) request.getAttribute("error");
            String success = (String) request.getAttribute("success");
        %>

        <% if (error != null) { %>
            <div class="mb-4 p-3 bg-red-100 text-red-700 rounded mt-10">
                <%= error %>
            </div>
        <% } %>

        <% if (success != null) { %>
            <div class="mb-4 p-3 bg-green-100 text-green-700 rounded mt-10">
                <%= success %>
            </div>
        <% } %>
    </div>
    <form action="${pageContext.request.contextPath}/searchtrips" method="post" class="bg-white p-6 rounded-2xl shadow-lg w-fit md:w-full md:max-w-6xl space-y-8">
        <div class="grid md:grid-cols-3 gap-4">
            <input type="text" name="depart" placeholder="Départ" list="startOptions" required
                   class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500">
                   <datalist id="startOptions">
                    <% 
                        List<String> startTowns = (List<String>) request.getAttribute("startTowns");
                        if (startTowns != null) {
                            List<String> firstFiveStartTowns = startTowns.subList(0, Math.min(5, startTowns.size()));
                            for (String town : firstFiveStartTowns) {
                    %>
                        <option value="<%= town %>"></option>
                    <%
                            }
                        }
                    %>
                  </datalist>
            <input type="text" name="destination" placeholder="Destination" list="endOptions" required
                   class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500">
                    <datalist id="endOptions">
                    <% 
                        List<String> endTowns = (List<String>) request.getAttribute("endTowns");
                        if (endTowns != null) {
                            List<String> firstFiveEndTowns = endTowns.subList(0, Math.min(5, endTowns.size()));
                            for (String town : firstFiveEndTowns) {
                    %>
                        <option value="<%= town %>"></option>
                    <%
                            }
                        }
                    %>
                  </datalist>
            <input type="date" name="date" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500" required>
        </div>

         <div class="flex justify-center">
            <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-semibold transition cursor-pointer">
                Rechercher un trajet
            </button>
        </div>
    </form>
    <div class="mt-12 text-center">
        <a href="${pageContext.request.contextPath}/createtrip"
           class="inline-block bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded-full font-semibold shadow-md">
            Proposer un trajet
        </a>
    </div>
</section>