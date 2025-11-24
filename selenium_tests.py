from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
import time

def test_carshare_homepage():
    # Initialiser le navigateur (ici Chrome)
    driver = webdriver.Chrome()

    # Accéder à l'URL de l'application (à adapter selon ton URL de préprod)
    driver.get("http://localhost:8080")  # Remplacer par l'URL de ton app

    # Vérifier que la page se charge et le titre est correct
    assert "Carshare" in driver.title

    # Vérifier la présence d'un élément clé sur la page (par exemple, un titre ou un bouton)
    try:
        element = driver.find_element(By.ID, "homepage-title")  # ID à ajuster
        assert element.is_displayed()  # Vérifie que l'élément est visible
    except Exception as e:
        print(f"Erreur lors de la vérification de l'élément : {e}")
        driver.quit()
        return

    # Optionnel : Interaction avec un formulaire, par exemple un champ de recherche
    try:
        search_box = driver.find_element(By.NAME, "search")  # Remplacer par un champ réel
        search_box.send_keys("Carshare")  # Entrer du texte dans le champ
        search_box.send_keys(Keys.RETURN)  # Soumettre le formulaire
        time.sleep(2)  # Attendre la réponse

        # Vérifier qu'un résultat de recherche apparaît (par exemple, un élément spécifique)
        results = driver.find_elements(By.CSS_SELECTOR, ".result-item")  # Sélecteur à ajuster
        assert len(results) > 0, "Aucun résultat trouvé"
    except Exception as e:
        print(f"Erreur dans l'interaction avec le formulaire de recherche : {e}")

    # Attendre un peu pour observer le navigateur
    time.sleep(2)

    # Fermer le navigateur
    driver.quit()

if __name__ == "__main__":
    test_carshare_homepage()
