import React, { useState } from 'react';
import axios from 'axios';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';


const Dashboard = () => {
  const [pin, setPin] = useState('');
  const [status, setStatus] = useState('');
  const [sessions, setSessions] = useState([]);
  const [submitted, setSubmitted] = useState(false);
  const [fromDate, setFromDate] = useState(null);
  const [toDate, setToDate] = useState(null);

  // const[name,setName]=useState('');

  const handleSubmit = async () => {
    if (!pin || pin.trim() === '') {
      alert("Please enter a PIN");
      return;
    }

    const numericPin = parseInt(pin, 10);
    if (isNaN(numericPin)) {
      alert("PIN must be a valid number");
      return;
    }

    try {
      const statusRes = await axios.get(`http://localhost:8080/attendance/${numericPin}/status`);
      const sessionsRes = await axios.get(`http://localhost:8080/attendance/${numericPin}/sessions`, {
        params: {
          from: fromDate ? fromDate.format('YYYY-MM-DD') : null,
          to: toDate ? toDate.format('YYYY-MM-DD') : null
        }
      });
      setStatus(statusRes.data);
      setSessions(sessionsRes.data);
      setSubmitted(true);
      localStorage.setItem('pin', pin);
    } catch (error) {
      console.error(error);
      alert('Invalid PIN or server error');
    }
  };

  return (
    <div className="pt-20 p-6 bg-gray-100 min-h-screen">
      <div className="p-6">
        {!submitted ? (
          <div className="pt-40 p-6 flex flex-col items-center">

            <Box
              component="form"
              sx={{ '& > :not(style)': { m: 1, width: '25ch' } }}
              noValidate
              autoComplete="off"
            ></Box>
            <TextField className="mb-4 p-2 border rounded w-full max-w-xs" id="standard-basic"
              label="Enter PIN"
              variant="standard"
              autoComplete="off"
              value={pin}
              onChange={(e) => setPin(e.target.value)}
            />


            {/* <input
  type="text"
  placeholder="Enter PIN"
  value={pin}
  onChange={(e) => setPin(e.target.value)}
  className="mb-4 p-2 border rounded w-full max-w-xs"
/> */}
            <div className="w-full max-w-xs mb-4 pt-5 pb-5">
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker
                  label="From"
                  value={fromDate}
                  onChange={(newValue) => setFromDate(newValue)}
                  slotProps={{
                    textField: {
                      variant: 'standard',
                      fullWidth: true,
                      sx: {
                        '& .MuiInputBase-root': {
                          padding: '4px 8px',
                        },
                      }
                    }
                  }}
                />
              </LocalizationProvider>
              <div className='pt-5 pb-5'>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="To"
                    value={toDate}
                    onChange={(newValue) => setToDate(newValue)}
                    slotProps={{
                      textField: {
                        variant: 'standard',
                        value: toDate,
                        fullWidth: true,
                        sx: {
                          '& .MuiInputBase-root': {
                            padding: '4px 8px',
                          },
                        }
                      }
                    }}
                  />
                </LocalizationProvider>
              </div>
            </div>

            <button onClick={handleSubmit} className="bg-blue-500 text-white px-4 py-2 rounded">
              Load Dashboard
            </button>
          </div>
        ) : (
          <div>
            <h2 className="text-xl font-bold mb-4">Dashboard for PIN: {pin}</h2>
            <p>Status: <strong>{status}</strong></p>
            <h3 className="mt-4 font-semibold">Attendance Sessions:</h3>
            <div className="overflow-x-auto mt-4">
              <table className="min-w-full bg-white border border-gray-200">
                <thead className="bg-gray-200 text-gray-700">
                  <tr>
                    <th className="py-2 px-4 border">Clock In</th>
                    <th className="py-2 px-4 border">Clock Out</th>
                    <th className="py-2 px-4 border">Hours</th>
                    <th className="py-2 px-4 border">Minutes</th>
                    <th className="py-2 px-4 border">Total (Raw)</th>
                    <th className="py-2 px-4 border">Total (Rounded)</th>
                  </tr>
                </thead>
                <tbody>
                  {sessions.map((session, index) => (
                    <tr key={index} className="text-center">
                      <td className="py-2 px-4 border">
                        {new Date(session.clockIn).toLocaleString('en-AU', {
                          year: 'numeric',
                          month: 'long',
                          day: 'numeric',
                          hour: '2-digit',
                          minute: '2-digit',
                        })}
                      </td>
                      <td className="py-2 px-4 border">
                        {session.clockOut ? new Date(session.clockOut).toLocaleString('en-AU', {
                          year: 'numeric',
                          month: 'long',
                          day: 'numeric',
                          hour: '2-digit',
                          minute: '2-digit',
                        }) : 'Still Clocked In'}
                      </td>

                      <td className="py-2 px-4 border">{session.hours}</td>
                      <td className="py-2 px-4 border">{session.minutes}</td>
                      <td className="py-2 px-4 border">{session.totalHours}</td>
                      <td className="py-2 px-4 border">{session.roundedHours}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
