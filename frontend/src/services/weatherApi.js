const API_BASE_URL = 'http://localhost:8080/api';

export const fetchWeather = async (city) => {
  try {
    const response = await fetch(
      `${API_BASE_URL}/weather?city=${encodeURIComponent(city)}`
    );

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('City not found. Please check the spelling.');
      } else if (response.status === 400) {
        throw new Error('Please enter a valid city name.');
      } else {
        throw new Error('Failed to fetch weather data. Please try again.');
      }
    }

    const data = await response.json();
    return data;
  } catch (error) {
    if (error.message.includes('fetch')) {
      throw new Error('Cannot connect to server. Make sure backend is running.');
    }
    throw error;
  }
};