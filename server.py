#!/usr/bin/env python3
import json
import os
from http.server import HTTPServer, SimpleHTTPRequestHandler
from urllib.parse import urlparse, parse_qs
from datetime import datetime, timedelta
import mimetypes

class NavetteProHandler(SimpleHTTPRequestHandler):
    def do_GET(self):
        """Handle GET requests"""
        parsed_path = urlparse(self.path)
        
        # Handle API endpoints
        if parsed_path.path.startswith('/api/'):
            self.handle_api_get(parsed_path.path)
            return
        
        # Serve static files
        if parsed_path.path == '/':
            parsed_path = parsed_path._replace(path='/index.html')
            self.path = '/index.html'
        
        return super().do_GET()
    
    def do_POST(self):
        """Handle POST requests"""
        parsed_path = urlparse(self.path)
        
        if parsed_path.path.startswith('/api/'):
            content_length = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(content_length)
            try:
                data = json.loads(body.decode('utf-8')) if body else {}
            except:
                data = {}
            
            self.handle_api_post(parsed_path.path, data)
            return
        
        return super().do_POST()
    
    def handle_api_get(self, path):
        """Handle API GET requests"""
        if path == '/api/routes':
            self.send_json(200, {
                'data': [
                    {
                        'id': 1,
                        'code': 'R01',
                        'name': 'Route Downtown',
                        'startPoint': 'Campus Mundiapolis',
                        'endPoint': 'Downtown Station',
                        'distance': 15.5,
                        'estimatedTime': '45 mins',
                        'schedule': ['07:00', '08:00', '09:00', '12:00', '16:00', '18:00']
                    },
                    {
                        'id': 2,
                        'code': 'R02',
                        'name': 'Route Airport',
                        'startPoint': 'Campus Mundiapolis',
                        'endPoint': 'Airport Terminal',
                        'distance': 25.0,
                        'estimatedTime': '60 mins',
                        'schedule': ['06:00', '07:30', '09:00', '18:00']
                    },
                    {
                        'id': 3,
                        'code': 'R03',
                        'name': 'Route New Medina',
                        'startPoint': 'Campus Mundiapolis',
                        'endPoint': 'New Medina Center',
                        'distance': 8.5,
                        'estimatedTime': '25 mins',
                        'schedule': ['07:00', '08:30', '10:00', '12:00', '14:00', '16:00', '18:00']
                    }
                ]
            })
        elif path.startswith('/api/routes/') and '/pickup-points' in path:
            route_id = path.split('/')[3]
            pickup_points = {
                '1': [
                    {'id': 1, 'name': 'Campus Main Gate', 'orderIndex': 1, 'latitude': 33.5731, 'longitude': -7.5898},
                    {'id': 2, 'name': 'Library', 'orderIndex': 2, 'latitude': 33.5745, 'longitude': -7.5890},
                    {'id': 3, 'name': 'Science Building', 'orderIndex': 3, 'latitude': 33.5755, 'longitude': -7.5875},
                    {'id': 4, 'name': 'Student Center', 'orderIndex': 4, 'latitude': 33.5720, 'longitude': -7.5910}
                ],
                '2': [
                    {'id': 5, 'name': 'Campus Main Gate', 'orderIndex': 1, 'latitude': 33.5731, 'longitude': -7.5898},
                    {'id': 6, 'name': 'Airport Road Intersection', 'orderIndex': 2, 'latitude': 33.5800, 'longitude': -7.6000},
                    {'id': 7, 'name': 'Airport Terminal', 'orderIndex': 3, 'latitude': 33.3736, 'longitude': -7.5898}
                ],
                '3': [
                    {'id': 8, 'name': 'Campus Main Gate', 'orderIndex': 1, 'latitude': 33.5731, 'longitude': -7.5898},
                    {'id': 9, 'name': 'Guéliz Center', 'orderIndex': 2, 'latitude': 31.6295, 'longitude': -7.9811},
                    {'id': 10, 'name': 'New Medina Plaza', 'orderIndex': 3, 'latitude': 31.6250, 'longitude': -7.9850}
                ]
            }
            self.send_json(200, pickup_points.get(route_id, []))
        elif path == '/api/bus-location':
            self.send_json(200, {
                'data': {
                    'busId': 1,
                    'routeId': 1,
                    'latitude': 33.5731,
                    'longitude': -7.5898,
                    'speed': 45,
                    'nextStop': 'Library',
                    'timeToNextStop': '5 mins',
                    'lastUpdate': datetime.now().isoformat()
                }
            })
        else:
            self.send_json(404, {'error': 'Endpoint not found'})
    
    def handle_api_post(self, path, data):
        """Handle API POST requests"""
        if path == '/api/auth/login':
            email = data.get('email', '')
            password = data.get('password', '')
            role = data.get('role', 'STUDENT')
            
            if not email or not password:
                self.send_json(400, {'error': 'Email and password required'})
                return
            
            # Mock successful login
            self.send_json(200, {
                'success': True,
                'token': f'mock-jwt-{email}-{datetime.now().timestamp()}',
                'userId': 123,
                'email': email,
                'firstName': 'Jean',
                'lastName': 'Dupont',
                'role': role
            })
        
        elif path == '/api/auth/register':
            email = data.get('email', '')
            password = data.get('password', '')
            firstName = data.get('firstName', '')
            lastName = data.get('lastName', '')
            
            if not email or not password or not firstName or not lastName:
                self.send_json(400, {'error': 'Missing required fields'})
                return
            
            # Mock successful register
            self.send_json(201, {
                'success': True,
                'token': f'mock-jwt-{email}-{datetime.now().timestamp()}',
                'userId': 456,
                'email': email,
                'firstName': firstName,
                'lastName': lastName,
                'role': 'STUDENT'
            })
        
        elif path == '/api/complaints':
            complaint = {
                'id': 999,
                'title': data.get('title', ''),
                'description': data.get('description', ''),
                'status': 'PENDING',
                'createdAt': datetime.now().isoformat()
            }
            self.send_json(201, {'success': True, 'complaint': complaint})
        
        else:
            self.send_json(404, {'error': 'Endpoint not found'})
    
    def send_json(self, status_code, data):
        """Send JSON response"""
        self.send_response(status_code)
        self.send_header('Content-type', 'application/json')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type, Authorization')
        self.end_headers()
        self.wfile.write(json.dumps(data).encode('utf-8'))
    
    def end_headers(self):
        """Add CORS headers and cache control"""
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type, Authorization')
        self.send_header('Cache-Control', 'no-store, no-cache, must-revalidate, max-age=0')
        self.send_header('Pragma', 'no-cache')
        self.send_header('Expires', '0')
        super().end_headers()
    
    def translate_path(self, path):
        """Serve files from frontend directory"""
        # Remove /api from path if present for static file serving
        if path.startswith('/api/'):
            return None
        
        # Change to frontend directory
        path = path.lstrip('/')
        words = path.split('/')
        words = filter(None, words)
        path = os.path.join('frontend', *words)
        
        return path

if __name__ == '__main__':
    os.chdir(os.path.dirname(os.path.abspath(__file__)))
    
    server_address = ('', 8080)
    httpd = HTTPServer(server_address, NavetteProHandler)
    print('=' * 50)
    print('NavettePro Server')
    print('=' * 50)
    print('Server running on http://localhost:8080')
    print('Press Ctrl+C to stop the server')
    print('=' * 50)
    
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print('\nServer stopped.')
        httpd.server_close()
