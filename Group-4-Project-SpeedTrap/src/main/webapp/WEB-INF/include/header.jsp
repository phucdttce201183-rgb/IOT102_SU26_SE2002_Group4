<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hệ thống Cảnh báo Giao thông</title>
        
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
        
        <style>
            /* Thêm một chút CSS cho giao diện mượt mà hơn */
            body { background-color: #f8f9fa; }
            .card { box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
        </style>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
            <div class="container-fluid">
                <a class="navbar-brand fw-bold text-warning" href="${pageContext.request.contextPath}/analytics">
                    🚦 Smart Traffic Monitor
                </a>
                
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent">
                    <span class="navbar-toggler-icon"></span>
                </button>
                
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/analytics">📊 Dashboard Thống Kê</a>
                        </li>
                    </ul>
                    <span class="navbar-text text-light fw-bold">
                        Dự án Java Web - Nhóm 4
                    </span>
                </div>
            </div>
        </nav>

        <main class="container mt-4 mb-5">