import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './Login';
import Welcome from './Welcome';
import Hostel from './Hostel';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/welcome" element={<Welcome />} />
        <Route path="/hostel" element={<Hostel />} />
        <Route path="*" element={<Navigate to="/" />} /> {/* Redirect all unknown routes to login */}
      </Routes>
    </Router>
  );
}

export default App;
