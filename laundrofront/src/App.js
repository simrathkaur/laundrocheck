// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import UserEmail from './UserEmail'; // Adjust the path based on your project structure

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/user-email" element={<UserEmail />} />
        {/* Other routes */}
      </Routes>
    </Router>
  );
}

export default App;
