
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'client') NOT NULL,
    email VARCHAR(100),
    telephone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE voiture (
    immatriculation VARCHAR(20) PRIMARY KEY,
    marque VARCHAR(50),
    modele VARCHAR(50),
    etat ENUM('en_marche', 'en_panne') DEFAULT 'en_marche',
    disponible BOOLEAN DEFAULT TRUE,
    couleur VARCHAR(20)
);


CREATE TABLE client (
    id_client INT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_client) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE location (
    id_location INT PRIMARY KEY AUTO_INCREMENT,
    immatriculation VARCHAR(20),
    id_client INT,
    date_debut DATE,
    date_fin DATE,
    retour_effectif DATE,
    penalite DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (immatriculation) REFERENCES voiture(immatriculation),
    FOREIGN KEY (id_client) REFERENCES client(id_client)
);


CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('client', 'admin') NOT NULL,  
    email VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);


INSERT INTO users (username, password, role, email, telephone) VALUES
('admin1', 'adminpass', 'admin', 'admin@example.com', '12 345 678'),

('client1', 'clientpass1', 'client', 'ahmed.benali@example.tn', '98 765 432'),
('client2', 'clientpass2', 'client', 'sarra.mhiri@example.tn', '97 654 321'),
('client3', 'clientpass3', 'client', 'walid.trabelsi@example.tn', '96 543 210'),
('client4', 'clientpass4', 'client', 'amel.bouazizi@example.tn', '95 432 109'),
('client5', 'clientpass5', 'client', 'imen.jaziri@example.tn', '94 321 098');


INSERT INTO client (id_client, nom, prenom) VALUES
(2, 'Nebli', 'Amine'),
(3, 'Rim', 'Guenidez'),
(4, 'Trabelsi', 'Walid'),
(5, 'Bouazizi', 'Amel'),
(6, 'Jaziri', 'Imen');

INSERT INTO voiture (immatriculation, marque, modele, etat, disponible, couleur) VALUES
('145-TUN-547', 'Renault', 'Clio', 'en_marche', TRUE, 'Blanc'),
('208-TUN-876', 'Peugeot', '208', 'en_panne', TRUE, 'Noir'),
('369-TUN-123', 'Volkswagen', 'Golf', 'en_marche', TRUE, 'Rouge'),
('420-TUN-789', 'Toyota', 'Corolla', 'en_panne', TRUE, 'Bleu'),
('512-TUN-456', 'Hyundai', 'i20', 'en_marche', TRUE, 'Gris'),
('678-TUN-234', 'Ford', 'Focus', 'en_marche', TRUE, 'Argent');

