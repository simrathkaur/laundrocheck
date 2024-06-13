import React, { useEffect, useState } from 'react';
import axios from 'axios';

function Welcome() {
  const [name, setName] = useState('');

  useEffect(() => {
    axios.get('http://localhost:8081/welcome')
      .then(response => {
        setName(response.data.name);
      })
      .catch(error => {
        console.error('There was an error fetching the user!', error);
      });
  }, []);

  return (
    <div>
      <h1>Welcome, {name}!</h1>
    </div>
  );
}

export default Welcome;
