import React from 'react';

function Login() {
  const googleLogin = () => {
    window.location.href = "http://localhost:8081/oauth2/authorization/google";
  };

  return (
    <div>
      <h1>Welcome to LaundroCheck</h1>
      <button onClick={googleLogin}>Login with Google</button>
    </div>
  );
}

export default Login;
