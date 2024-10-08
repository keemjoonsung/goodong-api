## goodong : Graduation Project - SKKU 2024

3D modelEntity repository web - combine with Blender Add-on (https://github.com/kjs990114/goodong-blender-addon)<br>
Blender Export and Import 3D modelEntity via website<br>
AI-based posting (auto generate title, description and tagEntities)

  
  <br>

## Project Objective

GitHub plays a significant role as a popular platform for software developers to
store, manage, share code, or showcase their portfolios. However, in the field of
3D modeling, there currently isn't a similarly popular platform now. By
establishing a repository for 3D modelEntities, designers can effectively manage their
work and use it as a portfolio to showcase their creations. It is expected to
provide innovative platforms for professionals in the 3D modeling field, thereby
fostering advancement within the industry.

<br>

## Preview

images will be uploaded soon 

<br>

## Project Progress

2024.01.23 - implementation register & login using JWT and Spring Security  <br>

2024.02.15 - User can upload 3D modelEntity ( .glb file) on website by making own repository<br>

2024.02.17 - User can view and interact rendered 3D modelEntity (implemented using Three.js)<br>

2024.02.20 - Update login functionality - User can keep logged in<br>

2024.05.09 - User can download uploaded 3D modelEntity<br>

2024.05.26  - User can just export 3D modelEntity to website via blender, and OpenAI fills title and description automatically.<br>

2024.06.25 - Separate repository of Backend source code and Frontend source code<br>

2024.06.27 - 	Change AI API (OpenAI API -> Gemini Vertex AI API) <br>

2024.06.29 - User can search repository (implemented using Elasticsearch)

2024.07.21 - deploy backend using Google Cloud Platform (cloud run, cloud build, cloud sql, cloude storage bucket), and CI / CD

2024.09.05 - User can commentEntity posts, likeEntity posts, followEntity users

2024.09.08 - Version control of Repository

2024.09.10 - User can generate title, contents and tagEntities automatically with Gemini API

<br>

## Structure


![goodong_struct](https://github.com/user-attachments/assets/bf5130b8-866d-46bc-8a60-4e77bb017f93)

<br>


## Budget
-
<br>


## Members

2018312075, SKKU Software Department, Kim joon sung (Back-end)<br>
2018311813, SKKU Software Department, Kim min jae (Front-end)

<br>



## Technical Stacks

frontend - React.js , Axios , Three.js

backend - Spring boot , JPA/Hibernate

databse - MySQL

security - Spring Security, JWT

api - Google Vertex AI (Gemini 1.5 flash)

infrastructure - Google Cloud Platform

<br>

## ERD
[ERD CLOUD](https://www.erdcloud.com/d/M9LBJgsyBpjDeoR3r)
