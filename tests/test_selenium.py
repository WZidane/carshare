from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
import time

def carshare_home():
    
    options = Options()
    options.add_argument("--headless")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")

    driver = webdriver.Chrome(
        service=Service(ChromeDriverManager().install()),
        options=options
    )

    # Accéder à l'URL de l'application
    driver.get("http://10.11.19.2:8090/carshare-app")

    # Vérifier que la page se charge et le titre est correct
    assert "Carshare" in driver.title

    # Vérifier la présence d'un élément sur la page
    try:
        element = driver.find_element(By.CSS_SELECTOR, "h1.text-4xl.font-bold")
        assert element.is_displayed()  # Vérifie que l'élément est visible
    except Exception as e:
        print(f"Erreur lors de la vérification de l'élément : {e}")
        driver.quit()
        return

    # Interaction avec un formulaire
    try:
        search_depart = driver.find_element(By.NAME, "depart")
        search_depart.send_keys("Paris")

        search_destination = driver.find_element(By.NAME, "destination")
        search_destination.send_keys("Nantes")
        
        search_date = driver.find_element(By.NAME, "date")
        search_date.send_keys("2025-06-18")

        submit_btn = driver.find_element(By.CSS_SELECTOR, "button[type='submit']")
        submit_btn.click()

        time.sleep(2)
    except Exception as e:
        print(f"Erreur dans l'interaction avec le formulaire de recherche : {e}")

    # Attendre un peu pour observer le navigateur
    time.sleep(2)

    # Fermer le navigateur
    driver.quit()

if __name__ == "__main__":
    carshare_home()