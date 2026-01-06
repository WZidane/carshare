<%@ page contentType="text/html;charset=UTF-8" import="app.model.User" language="java" isELIgnored="false" %>

<%
    User user = (User) session.getAttribute("user");
    String username = (String) user.getUsername();
    String email = (String) user.getEmail();
    String avatar = (String) user.getAvatar();
    
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>

<section class="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-blue-100 to-white">
    <div class="bg-white w-full p-8">

        <h1 class="text-2xl font-bold mb-6 text-center">Bienvenue, <%= username %> !</h1>

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

        <div id="read-profile" class="space-y-6 max-w-4xl mx-auto">
            <div class="flex justify-center items-center">
                <div class="relative flex items-center justify-center w-[165px] h-[165px] transition-all duration-300">
                    <img id="avatar_image" src="<%= avatar %>" alt="Avatar" class="absolute w-[165px] h-[165px] object-cover border-2 border-blue-500 rounded-full" />
                </div>
            </div>
            <p><strong>Nom d'utilisateur :</strong> <%= username %></p>
            <p><strong>Adresse e-mail :</strong> <%= email %></p>
        </div>

        <div id="edit-profile">
            <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data" class="space-y-6 max-w-4xl mx-auto">

                <div class="flex justify-center items-center">
                    <div class="relative flex items-center justify-center w-[165px] h-[165px] transition-all duration-300">
                        <input type="file" id="avatar" name="avatar" accept="image/*" class="hidden" />
                        <img id="avatarPreview" src="<%= avatar %>" alt="Avatar" class="absolute w-[165px] h-[165px] object-cover border-2 border-blue-500 rounded-full" />
                        <label for="avatar" class="absolute flex items-center justify-center w-[165px] h-[165px] cursor-pointer transition-all duration-200 bg-transparent text-transparent rounded-full hover:bg-blue-500 hover:text-white z-10">Changer l'avatar</label>
                    </div>
                </div>

                <div>
                    <label for="username" class="block mb-1 font-semibold text-gray-700">Nom d'utilisateur <span class="text-red-700">*</span></label>
                    <input type="text" id="username" name="username" required
                        placeholder="Nom d'utilisateur"
                        value="<%= username %>"
                        class="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <div>
                    <label for="email" class="block mb-1 font-semibold text-gray-700">Email <span class="text-red-700">*</span></label>
                    <input type="email" id="email" name="email" required
                        placeholder="Adresse e-mail"
                        value="<%= email %>"
                        class="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <div>
                    <h2 class="text-xl my-4 text-center">Changement de mot de passe</h2>
                    <label for="password" class="block mb-1 font-semibold text-gray-700">Nouveau mot de passe</label>
                    <input type="password" id="password" name="password" placeholder="Laissez vide pour ne pas changer de mot de passe..."
                        class="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <div class="text-center">
                    <button id="save-button" type="submit" 
                            class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 mt-4 cursor-pointer">
                        Enregistrer
                    </button>
                </div>
            </form>
        </div>

        <div class="text-center">
            <button id="edit-button" type="button" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 mt-4 cursor-pointer">
                Modifier
            </button>
        </div>
    </div>
</section>