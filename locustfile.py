from locust import HttpUser, task, between

# Classe représentant un utilisateur virtuel
class WebsiteUser(HttpUser):
    # Temps d'attente entre chaque tâche (en secondes)
    wait_time = between(1, 5)  # attendre entre 1 et 5 secondes entre les actions

    # La tâche principale : tester la page d'accueil
    @task
    def load_homepage(self):
        self.client.get("/")  # Simuler une requête GET sur la page d'accueil

    # Autre tâche : tester la connexion de l'utilisateur
    @task(2)  # Cette tâche est plus "importante" (elle sera appelée 2x plus souvent)
    def login(self):
        # Simuler une requête POST sur la page de connexion avec un jeu de données fictif
        self.client.post("/login", json={"email": "testuser", "password": "password123"})
        self.client.post("/login", json={"email": "testLocust@example.com", "password": "testLocustpasswd"})


