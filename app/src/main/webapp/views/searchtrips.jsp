<%@ page contentType="text/html;charset=UTF-8" import="java.util.List, java.util.ArrayList, app.model.Trip" %>

<section class="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-blue-100 to-white">
    <div class="size-2/3 mx-auto bg-white p-6 rounded-xl shadow-lg mt-10">
        <h1 class="text-2xl font-bold mb-6 text-center">Rechercher un trajet</h1>

        <form action="searchtrips" method="post" class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
            <input type="text" name="depart" placeholder="Départ" required
                   value="<%= request.getAttribute("depart") != null ? request.getAttribute("depart") : "" %>"
                   class="px-4 py-2 border rounded-lg">
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

            <input type="text" name="destination" placeholder="Destination" required
                   value="<%= request.getAttribute("destination") != null ? request.getAttribute("destination") : "" %>"
                   class="px-4 py-2 border rounded-lg">
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

            <input type="date" name="date" required
                   value="<%= request.getAttribute("date") != null ? request.getAttribute("date") : "" %>"
                   class="px-4 py-2 border rounded-lg">

            <div class="col-span-full text-center">
                <button type="submit" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 cursor-pointer">
                    Rechercher
                </button>
            </div>
        </form>
    </div>

        <%
            List<Trip> trips = (List<Trip>) request.getAttribute("trips");

            if (trips == null || trips.isEmpty()) {
        %>
            <p class="text-center text-gray-500">Aucun trajet trouvé pour ces critères.</p>
        <% } else { %>
            <div class="container mx-auto px-4 py-6 flex flex-col gap-10">
                <% for (Trip trip : trips) { %>
                    <div class="bg-white p-6 rounded-2xl shadow hover:shadow-lg transition">
                        <div class="flex justify-between items-center mb-2">
                            <span class="text-lg font-semibold text-blue-600"><%= trip.getStartTown() %></span>
                            <svg class="h-5 w-5 text-gray-400 mx-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                            </svg>
                            <span class="text-lg font-semibold text-green-600"><%= trip.getEndTown() %></span>
                        </div>
                        <div class="text-sm text-gray-600 mb-2">
                            Départ : <strong><%= trip.getFormattedStartDate() %></strong><br>
                            Places disponibles : <%= trip.getNbPlaces() %><br>
                            Prix : <span class="text-blue-500 font-bold"><%= trip.getPrice() %> €</span>
                        </div>
                        <a href="<%= request.getContextPath() %>/tripdetails?id=<%= trip.getId() %>"
                        class="inline-block px-6 py-2 bg-blue-600 text-white font-semibold rounded hover:bg-blue-700">
                            Détails
                        </a>
                    </div>
                <% } %>
            </div>
        <% } %>
    </div>
</section>