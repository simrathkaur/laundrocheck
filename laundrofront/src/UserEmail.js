import React, { useState, useEffect } from 'react';
import axios from 'axios';

const UserEmail = () => {
    const [email, setEmail] = useState('');
    const [machines, setMachines] = useState([]);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get('http://localhost:8081/user-info', { withCredentials: true });
                setEmail(response.data);

                const machineResponse = await axios.get('http://localhost:8081/api/status/machine-status', { withCredentials: true });
                setMachines(machineResponse.data);
            } catch (error) {
                console.error('Error fetching user data:', error.response ? error.response.data : error.message);
            }
        };

        fetchUserData();
    }, []);

    const handleMachineClick = async (machineId, action) => {
        try {
            const response = await axios.put(`http://localhost:8081/api/status/machine/${machineId}/${action}`, {}, { withCredentials: true });
            // Assuming your API updates the machine status and returns updated data
            setMachines(prevMachines => prevMachines.map(machine => 
                machine.id === machineId ? response.data : machine
            ));
        } catch (error) {
            console.error('Error updating machine status:', error.response ? error.response.data : error.message);
        }
    };

    return (
        <div>
            <h1>User Email</h1>
            <p>{email ? `Logged in as: ${email}` : 'Not logged in'}</p>
            <div>
                {machines.map(machine => (
                    <div key={machine.id}>
                        <h3>Machine ID: {machine.id}</h3>
                        <p>Status: {machine.available ? 'Available' : machine.inUse ? 'In Use' : 'Done'}</p>
                        {machine.available && (
                            <button style={{ backgroundColor: 'green' }} onClick={() => handleMachineClick(machine.id, 'start')}>
                                Click to use
                            </button>
                        )}
                        {machine.inUse && (
                            <button style={{ backgroundColor: 'red' }} onClick={() => handleMachineClick(machine.id, 'done')}>
                                Click if machine is done
                            </button>
                        )}
                        {machine.done && (
                            <button style={{ backgroundColor: 'yellow' }} onClick={() => handleMachineClick(machine.id, 'unload')}>
                                Click if unloaded
                            </button>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default UserEmail;
