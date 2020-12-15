-- This creates the imageapi database on PostGreQL and the corresponding IMAGE table to hold uploaded images
-- Please have a valid Docker Container Running PostGreSQL to create this table
-- Please update quarkus.datasource.jdbc.url in the quarkus imagestorage service
-- Or alternatively specify a valid IMAGEAPI_DATASOURCE environment property to the database

CREATE DATABASE imageapi WITH OWNER = postgres ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8';
\c imageapi
CREATE TABLE IF NOT EXISTS IMAGE (imageid bigserial PRIMARY KEY, imagehashid VARCHAR(256) UNIQUE, img bytea, imgthumbnail bytea, dateadded timestamp);