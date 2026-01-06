<%@ page contentType="text/html;charset=UTF-8" import="app.model.User, app.model.Trip" language="java" isELIgnored="false" %>

<%
    Trip trip = (Trip) request.getAttribute("trip");
    User user = (User) session.getAttribute("user");
    boolean isOwner = trip.getUserId() == user.getId();
    boolean hasPlaces = trip.getNbPlaces() > 0;
    Boolean alreadyBooked = (Boolean) request.getAttribute("alreadyBooked");

    String error = (String) session.getAttribute("error");
    String success = (String) session.getAttribute("success");
%>

<section class="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-blue-100 to-white">
    <div class="size-2/3 mx-auto bg-white p-8 mt-20 rounded-xl shadow">
        <% if (trip == null) { %>
            <p class="text-red-600">Le trajet est introuvable.</p>
        <% } else { %>
            <h2 class="text-2xl font-bold text-center text-blue-700 mb-6">Détail du trajet</h2>

            <div class="space-y-4 divide-y divide-gray-200">
                <div class="flex items-start justify-between">
                    <span class="text-gray-600 font-medium w-1/3">Départ :</span>
                    <span class="text-gray-900 w-2/3"><%= trip.getStartTown() %> - <%= trip.getStartAddress() != null ? trip.getStartAddress() : "Adresse non précisée." %></span>
                </div>

                <div class="flex items-start justify-between pt-4">
                    <span class="text-gray-600 font-medium w-1/3">Arrivée :</span>
                    <span class="text-gray-900 w-2/3"><%= trip.getEndTown() %> - <%= trip.getEndAddress() != null ? trip.getEndAddress() : "Adresse non précisée." %></span>
                </div>

                <div class="flex items-start justify-between pt-4">
                    <span class="text-gray-600 font-medium w-1/3">Date :</span>
                    <span class="text-gray-900 w-2/3"><%= trip.getFormattedStartDate() %></span>
                </div>

                <div class="flex items-start justify-between pt-4">
                    <span class="text-gray-600 font-medium w-1/3">Places :</span>
                    <span class="text-gray-900 w-2/3"><%= trip.getNbPlaces() %></span>
                </div>

                <div class="flex items-start justify-between pt-4">
                    <span class="text-gray-600 font-medium w-1/3">Prix :</span>
                    <span class="text-blue-600 font-semibold w-2/3"><%= trip.getPrice() %> €</span>
                </div>

                <div class="flex items-start justify-between pt-4">
                    <span class="text-gray-600 font-medium w-1/3">Créateur du trajet :</span>
                    <span class="text-gray-900 w-2/3"><%= trip.getUsername() %></span>
                </div>

                <div class="flex items-start justify-between pt-4">
                    <span class="text-gray-600 font-medium w-1/3">Véhicule :</span>
                    <span class="text-gray-900 w-2/3"><%= trip.getVehicule() != null ? trip.getVehicule() : "Véhicule non précisé." %></span>
                </div>

                <div class="pt-4">
                    <span class="text-gray-600 font-medium block mb-1">Description :</span>
                    <p class="text-gray-800 bg-gray-100 p-3 rounded-md"><%= trip.getDescription() != null ? trip.getDescription() : "Aucune description." %></p>
                </div>
            </div>

            <% if (error != null) { %>
                <div class="mb-4 p-3 bg-red-100 text-red-700 mt-10 rounded">
                    <%= error %>
                </div>
            <%
                    session.removeAttribute("error");
                }
            %>
            <% if (success != null) { %>
                <div class="mb-4 p-3 bg-green-100 text-green-700 mt-10 rounded">
                    <%= success %>
                </div>
             <%
                    session.removeAttribute("success");
                }
             %>

            <div class="mt-6 flex justify-center gap-4">
                <% if (isOwner) { %>
                    <button type="button" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 cursor-pointer" onclick="openDeleteModal()">
                        Supprimer le trajet
                    </button>
                <% } %>
                    
                <% if(!isOwner && alreadyBooked) { %>
                    <form action="<%= request.getContextPath() %>/cancelbooktrip" method="post">
                        <input type="hidden" name="trip_id" value="<%= trip.getId() %>">
                        <button type="submit" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 cursor-pointer">
                            Annuler la réservation
                        </button>
                    </form>
                <% } else if(!isOwner && hasPlaces && !alreadyBooked) { %>
                    <form method="post" action="<%= request.getContextPath() %>/booktrip">
                        <input type="hidden" name="trip_id" value="<%= trip.getId() %>" />
                        <button type="submit" class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded cursor-pointer">Réserver</button>
                    </form>
                <% } %>
            </div>
                <% if(!hasPlaces) { %>
                    <div class="mt-6 flex justify-center gap-4">
                        <p class="text-red-600 font-semibold">Plus de place disponible.</p>
                    </div>
                <% } %>
        <% } %>
    </div>
</section>

<div id="deleteModal"
     class="fixed inset-0 z-50 hidden flex items-center justify-center bg-gray-950/80 transition-opacity duration-300 ease-in-out"
     aria-hidden="true">
    <div id="modalContent"
         class="bg-white rounded-lg p-6 shadow-xl max-w-sm w-full transform scale-95 opacity-0 transition-all duration-300 ease-in-out">
        <h2 class="text-xl font-semibold mb-4 text-center text-red-600">Confirmer la suppression</h2>
        <p class="mb-6 text-center text-gray-700">
            Êtes-vous sûr de vouloir supprimer ce trajet ? Cette action est irréversible.
        </p>
        <div class="flex justify-between gap-4">
            <button onclick="closeDeleteModal()"
                    class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400 text-gray-800 cursor-pointer">
                Annuler
            </button>
            <form action="<%= request.getContextPath() %>/deletetrip" method="post">
                <input type="hidden" name="trip_id" value="<%= trip.getId() %>">
                <button type="submit"
                        class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 cursor-pointer">
                    Supprimer
                </button>
            </form>
        </div>
    </div>
</div>