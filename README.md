<div id="top"></div>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  
<h1 align="center">Solita-warehouse</h1>

<h3 align="center">
    This project was created on the courses Softala-projekti / Multidisciplinary Software Project<br />
<br />
</div>

## About the project

This is a collaborative work between Solita and HAAGA-HELIA School of Applied Science which aims at building a mobile warehouse application for item loaning/borrowing which works through Odoo ERP and OpenCV machine vision.

## Docker Installation

1. Download the rental_base app: https://apps.odoo.com/apps/modules/14.0/rental_base/
2. Copy path to the installed folder eg. **C:\odoo\rental_base**
3. Run in cmd the following command:
```sh
docker run -d -e POSTGRES_USER=odoo -e POSTGRES_PASSWORD=odoo -e POSTGRES_DB=postgres -p 5432:5432 --name db postgres:15
```
4. Run the following command in cmd. Pay attention to to the path you installed the rental_base to.
```sh
docker run -v <YOUR_RENTAL_BASE_PATH_HERE>:/mnt/extra-addons -p 8069:8069 --name odoo14 --link db:db -t odoo:14
```
eg.
```sh
docker run -v C:\odoo\rental_base:/mnt/extra-addons -p 8069:8069 --name odoo14 --link db:db -t odoo:14
```
5. Start Docker containers if not automatically started.
6. Open Odoo in your browser via http://localhost:8069
7. Fill the form to access Odoo. Pay attention to: Master Password, Database Name, Email and Password. **Demo data** recommended for testing purposes.
8. Install rental base module in Odoo.

## Application Installation

1. Install the newest Android Studio version Giraffe | 2022.3.1 Patch 1 or newer.
2. Clone the repository
   ```sh
   https://github.com/Solita-Warehouse/solita-warehouse.git
   ```
3. Make sure you have necessary **env** file. Save it inside **\app\src\main\assets** folder.
4. Run MainActivity.kt to start the application.
