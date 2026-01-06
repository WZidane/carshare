<%@ page contentType="text/html;charset=UTF-8" import="java.time.LocalDateTime" language="java" isELIgnored="false" %>

<%
    LocalDateTime now = java.time.LocalDateTime.now();
    String minDateTime = now.toString().replace("T", " ").substring(0, 16).replace(" ", "T");

    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>

<section class="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-blue-100 to-white">
    <div class="size-2/3 mx-auto mt-10 bg-white p-8 rounded-xl shadow-md mt-20 mb-20">
        <h1 class="text-2xl font-semibold mb-6 text-center">Créer un trajet</h1>

        <form action="${pageContext.request.contextPath}/createtrip" method="post" class="space-y-5">

            <% if (error != null) { %>
                <div class="mb-4 p-3 bg-red-100 text-red-700 rounded">
                    <%= error %>
                </div>
            <% } %>
            <% if (success != null) { %>
                <div class="mb-4 p-3 bg-green-100 text-green-700 rounded">
                    <%= success %>
                </div>
            <% } %>

            <div>
                <label for="trip_type" class="block font-medium">Type de trajet <span class="text-red-700">*</span></label>
                <select name="trip_type" id="trip_type" required
                        class="w-full border border-gray-300 rounded-md px-3 py-2">
                    <option value="1">Conducteur</option>
                    <option value="0">Passager</option>
                </select>
            </div>

            <div>
                <label for="start_town" class="block font-medium">Ville de départ <span class="text-red-700">*</span></label>
                <input type="text" id="start_town" name="start_town" required
                    class="w-full border border-gray-300 rounded-md px-3 py-2"/>
            </div>

            <div>
                <label for="start_address" class="block font-medium">Adresse de départ</label>
                <input type="text" id="start_address" name="start_address"
                    class="w-full border border-gray-300 rounded-md px-3 py-2"/>
            </div>

            <div>
                <label for="end_town" class="block font-medium">Ville d’arrivée <span class="text-red-700">*</span></label>
                <input type="text" id="end_town" name="end_town" required
                    class="w-full border border-gray-300 rounded-md px-3 py-2"/>
            </div>

            <div>
                <label for="end_address" class="block font-medium">Adresse d’arrivée</label>
                <input type="text" id="end_address" name="end_address"
                    class="w-full border border-gray-300 rounded-md px-3 py-2"/>
            </div>

            <div>
                <label for="start_date" class="block font-medium">Date et heure de départ <span class="text-red-700">*</span></label>
                <input type="datetime-local" id="start_date" name="start_date" required min="<%= minDateTime %>"
                    class="w-full border border-gray-300 rounded-md px-3 py-2"/>
            </div>

            <div>
                <label for="nb_places" class="block font-medium">Nombre de places <span class="text-red-700">*</span></label>
                <input type="number" id="nb_places" name="nb_places" min="1" inputmode="numeric" pattern="[0-9]*" required
                    class="w-full border border-gray-300 rounded-md px-3 py-2"/>
            </div>

            <div>
                <label for="price" class="block font-medium">Prix (€) <span class="text-red-700">*</span></label>
                <input type="number" id="price" name="price" step="0.01" min="0" required
                    class="w-full border border-gray-300 rounded-md px-3 py-2"/>
            </div>

            <div>
                <label for="vehicule" class="block font-medium">Véhicule <span class="text-red-700">*</span></label>
                <input type="text" id="vehicule" name="vehicule" required
                    class="w-full border border-gray-300 rounded-md px-3 py-2"/>
            </div>

            <div>
                <label for="description" class="block font-medium">Description</label>
                <textarea id="description" name="description" rows="4"
                        class="w-full border border-gray-300 rounded-md px-3 py-2"></textarea>
            </div>

            <div class="text-center">
                <button type="submit"
                        class="bg-blue-600 text-white px-6 py-2 rounded-md hover:bg-blue-700 transition cursor-pointer">
                    Créer le trajet
                </button>
            </div>
        </form>
    </div>
</section>