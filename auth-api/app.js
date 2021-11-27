import express from 'express';

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

app.get('/api/status', (req, res) => {
    return res.status(200).json({
        services: 'Auth-API',
        status: "up",
        httpStatus: 200
    });
})

app.listen(PORT, () => {
    console.info(`Server started successfully at Port ${PORT}`)
})