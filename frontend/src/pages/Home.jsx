import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useNavigation } from 'react-router-dom';
import axios from 'axios';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';

export const Home = () => {
  const[pin,setPin] = useState('');
  const[error,setError] = useState('');
  const[status,setStatus] = useState('');
  const navigate = useNavigate();

const handleSubmit = async () => {
  try {
    const response = await axios.post('http://localhost:8080/attendance/record', null, {
      params: { pin: pin }
    });
    const statusResponse = await axios.get(`http://localhost:8080/attendance/${pin}/status`);
    const status = statusResponse.data;
    console.log(response.data);
     setStatus(`✅ ${status}`);
    setError('');
  } catch (error) {
    console.error(error);
    setError('Invalid PIN or server error');
    setStatus('')
  }
};


  return (
      <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-4">
      <h2 className="text-2xl font-bold mb-4">Welcome to ClockMate</h2>
     <div className="flex flex-col items-center space-y-10">
     <Box
      component="form"
      sx={{ '& > :not(style)': { m: 1, width: '25ch' } }}
      noValidate
      autoComplete="off"
    ></Box>
      <TextField id="standard-basic" 
      label="ENTER YOUR PIN"
      variant="standard" 
        autoComplete="off"
      value={pin}
      onChange={(e)=>setPin(e.target.value)}
      />
      {status && <p className="text-green-600 mt-4 font-semibold">{status}</p>}
       {error && <p className="text-red-500 mb-2">{error}</p>}
       
      <Button variant="contained"  color="success" onClick={handleSubmit}>Clock-In/Clock-Out</Button>
      <Link to="/register"><Button variant="contained" color="primary">Register</Button></Link>


</div>
    </div>
  );
};
export default Home;
