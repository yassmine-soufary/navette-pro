<?php
// Simple mock API for NavettePro frontend
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization');
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Parse request
$request_uri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$request_method = $_SERVER['REQUEST_METHOD'];
$request_body = json_decode(file_get_contents('php://input'), true) ?? [];

// Simple routing
if (strpos($request_uri, '/api/auth/login') !== false && $request_method === 'POST') {
    // Mock login
    $response = [
        'success' => true,
        'token' => 'mock-jwt-token-' . time(),
        'user' => [
            'id' => 1,
            'username' => $request_body['username'] ?? 'user',
            'email' => $request_body['username'] . '@navettepro.com',
            'role' => 'ROLE_STUDENT'
        ]
    ];
    echo json_encode($response);
    exit;
}

if (strpos($request_uri, '/api/auth/register') !== false && $request_method === 'POST') {
    // Mock registration
    $response = [
        'success' => true,
        'message' => 'User registered successfully',
        'user' => [
            'id' => 2,
            'username' => $request_body['username'] ?? 'newuser',
            'email' => $request_body['email'] ?? 'newuser@navettepro.com',
            'role' => 'ROLE_STUDENT'
        ]
    ];
    echo json_encode($response);
    exit;
}

if (strpos($request_uri, '/api/routes') !== false && $request_method === 'GET') {
    // Mock routes list
    $routes = [
        [
            'id' => 1,
            'name' => 'Route Downtown',
            'startPoint' => 'Campus',
            'endPoint' => 'Downtown Station',
            'distance' => 15.5,
            'estimatedTime' => '45 mins'
        ],
        [
            'id' => 2,
            'name' => 'Route Airport',
            'startPoint' => 'Campus',
            'endPoint' => 'Airport Terminal',
            'distance' => 25.0,
            'estimatedTime' => '60 mins'
        ]
    ];
    echo json_encode(['data' => $routes]);
    exit;
}

if (strpos($request_uri, '/api/bus-location') !== false && $request_method === 'GET') {
    // Mock bus location
    $location = [
        'busId' => 1,
        'latitude' => 33.5731,
        'longitude' => -7.5898,
        'speed' => 45,
        'routeId' => 1,
        'nextStop' => 'Central Bus Station',
        'timeToNextStop' => '5 mins'
    ];
    echo json_encode(['data' => $location]);
    exit;
}

if (strpos($request_uri, '/api/complaints') !== false && $request_method === 'POST') {
    // Mock complaint submission
    $response = [
        'success' => true,
        'message' => 'Complaint submitted successfully',
        'complaintId' => 123
    ];
    echo json_encode($response);
    exit;
}

// Default 404
http_response_code(404);
echo json_encode(['error' => 'Endpoint not found']);
?>
