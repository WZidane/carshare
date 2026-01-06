<%@ page contentType="text/html;charset=UTF-8" import="java.util.List, app.model.Trip" language="java" isELIgnored="false" %>

<section class="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-blue-100 to-white">
    <div class="container mx-auto px-4 py-6">
        <h1 class="text-3xl font-bold text-center mb-20">Mes réservations</h1>

        <%
            List<Trip> bookings = (List<Trip>) request.getAttribute("bookings");

            if (bookings == null || bookings.isEmpty()) {
        %>
            <p class="text-center text-gray-600">Vous n'avez pas encore de réservation.</p>

        <% } else { %>
            <div class="container mx-auto px-4 py-6 flex flex-col gap-10">
            
            <%
                 for (Trip booking : bookings) {
            %>
                <div class="bg-white rounded-2xl shadow p-5 hover:shadow-lg transition">
                    <div class="flex justify-between items-center mb-2">
                        <span class="text-lg font-semibold text-blue-600"><%= booking.getStartTown() %></span>
                        <svg class="h-5 w-5 text-gray-400 mx-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                        </svg>
                        <span class="text-lg font-semibold text-green-600"><%= booking.getEndTown() %></span>
                    </div>
                    <div class="text-sm text-gray-600 mb-2">
                        Départ : <strong><%= booking.getFormattedStartDate() %></strong><br>
                        Places disponibles : <%= booking.getNbPlaces() %><br>
                        Prix : <span class="text-blue-500 font-bold"><%= booking.getPrice() %> €</span>
                    </div>
                    <a href="<%= request.getContextPath() %>/tripdetails?id=<%= booking.getId() %>" 
                       class="inline-block px-6 py-2 bg-blue-600 text-white font-semibold rounded hover:bg-blue-700">
                       Détails
                    </a>
                </div>
            <%
                }
             }
            %>
            </div>
</section>