<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
    
<section class="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-blue-100 to-white">
    <div class="bg-white shadow-lg rounded-xl p-8 w-full max-w-md">
        <h2 class="text-2xl font-bold text-center text-blue-600 mb-6">Connexion</h2>

        <form action="${pageContext.request.contextPath}/login" method="post" class="space-y-4">
            <div>
                <label for="email" class="block text-gray-700 font-semibold mb-1">Adresse e-mail <span class="text-red-700">*</span></label>
                <input type="email" name="email" id="email" placeholder="Adresse e-mail" required
                        class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>

            <div>
                <label for="password" class="block text-gray-700 font-semibold mb-1">Mot de passe <span class="text-red-700">*</span></label>
                <input type="password" name="password" id="password" placeholder="Mot de passe" required
                        class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>

            <% 
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");
            %>

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


            <button type="submit"
                    class="w-full bg-blue-600 text-white font-semibold py-2 rounded-lg hover:bg-blue-700 transition cursor-pointer">
                Se connecter
            </button>

            <p class="text-center text-sm text-gray-600 mt-4">
                Pas encore de compte ?
                <a href="${pageContext.request.contextPath}/register" class="text-blue-600 hover:underline">Inscrivez-vous</a>
            </p>
        </form>
    </div>
</section>