import React from 'react';

function Hostel() {
  return (
    <div>
      <h1>Hostel Page</h1>
      <button onClick={() => alert('Girls Hostel')}>Girls Hostel</button>
      <button onClick={() => alert('Boys Hostel')}>Boys Hostel</button>
    </div>
  );
}

export default Hostel;
