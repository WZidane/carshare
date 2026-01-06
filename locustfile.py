from locust import HttpUser, task, between

class WebsiteUser(HttpUser):
    wait_time = between(1, 5)  # Attendre entre 1 et 5 secondes entre les tâches

    @task
    def index(self):
        self.client.get("/carshare-app") # Test de la page d'accueil

    @task(3)  # Cette tâche sera potentiellement appelée 3 fois plus souvent. Cela permet de simuler les pages qui seront le plus souvent sollicitées
    def login(self):
        self.client.post("/carshare-app/login", json={"email": "bob@example.com", "password": "12345"})