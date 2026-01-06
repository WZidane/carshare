<%@ page contentType="text/html;charset=UTF-8" import="java.util.List, app.model.Trip" language="java" isELIgnored="false" %>

<section class="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-blue-100 to-white">
    <div class="container mx-auto px-4 py-6">

        <%
            List<Trip> driverTrips = (List<Trip>) request.getAttribute("driverTrips");
        %>

         <h1 class="text-3xl font-bold text-center text-blue-700 mb-40">Trajets Conducteur</h1>

        <% if (driverTrips != null && !driverTrips.isEmpty()) { %>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <% for (Trip trip : driverTrips) { %>
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
        <% } else { %>
            <p class="text-center text-gray-500 text-lg">Aucun trajet disponible pour le moment.</p>
        <% } %>
    </div>
</section>