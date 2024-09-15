## goodong : Graduation Project - SKKU 2024

3D model repository web - combine with Blender Add-on (https://github.com/kjs990114/goodong-blender-addon)<br>
Blender Export and Import 3D model via website<br>
AI-based posting (auto generate title, description and tags)

  
  <br>

## Project Objective

GitHub plays a significant role as a popular platform for software developers to
store, manage, share code, or showcase their portfolios. However, in the field of
3D modeling, there currently isn't a similarly popular platform now. By
establishing a repository for 3D models, designers can effectively manage their
work and use it as a portfolio to showcase their creations. It is expected to
provide innovative platforms for professionals in the 3D modeling field, thereby
fostering advancement within the industry.

<br>

## Preview

images will be uploaded soon 

<br>

## Project Progress

2024.01.23 - implementation register & login using JWT and Spring Security  <br>

2024.02.15 - User can upload 3D model ( .glb file) on website by making own repository<br>

2024.02.17 - User can view and interact rendered 3D model (implemented using Three.js)<br>

2024.02.20 - Update login functionality - User can keep logged in<br>

2024.05.09 - User can download uploaded 3D model<br>

2024.05.26  - User can just export 3D model to website via blender, and OpenAI fills title and description automatically.<br>

2024.06.25 - Separate repository of Backend source code and Frontend source code<br>

2024.06.27 - 	Change AI API (OpenAI API -> Gemini Vertex AI API) <br>

2024.06.29 - User can search repository (implemented using Elasticsearch)

2024.07.21 - deploy backend using Google Cloud Platform (cloud run, cloud build, cloud sql, cloude storage bucket), and CI / CD

2024.09.05 - User can comment posts, like posts, follow users

2024.09.08 - Version control of Repository

2024.09.10 - User can generate title, contents and tags automatically with Gemini API

<br>

## Structure
![스크린샷 2024-06-05 오후 11 14 18](https://github.com/kjs990114/goodong/assets/50402527/dba6201a-1152-4da4-94fe-890a1e06c1eb)

<br>


## Budget
-
<br>


## Members

2018312075, Software Department, Kim joon sung (Back-end)<br>
2018311813, Software Department, Kim min jae (Front-end)

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
