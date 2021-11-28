import UserRepository from "../repository/userRepository.js";
import * as httpStatus from '../../../config/constants/httpStatus.js'
import UserException from "../exception/UserException.js";
import bcrypt from "bcrypt"
import jwt from 'jsonwebtoken'
import * as secrets from '../../../config/constants/secrets.js'

class UserService {
    async getAcessToken(req) {
        try {
            const { email, password } = req.body;
            this.validateAcessTokenData(email, password)
            let user = await UserRepository.findByEmail(email);
            this.validateAcessTokenData(user.email, user.password);
            await this.validatePassword(password, user.password);
            const authUser = { id: user.id, name: user.name, email: user.email };
            const accessToken = jwt.sign({authUser}, secrets.API_SECRET,{expiresIn: '1d'});
            return {
                status: httpStatus.SUCCESS,
                accessToken
            }
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message
            }
        }
        
    }

    async findByEmail(req) {
        try {
            const { email } = req.params
            const { authUser } = req;
            this.validateRequestData(email)

            let user = await UserRepository.findByEmail(email);
            this.validateUserNotFound(user)
            this.validateAuthenticatedUser(user, authUser)
            return {
                status: httpStatus.SUCCESS,
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email
                }
            }
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message
            }
        }
    }

    validateAuthenticatedUser(user, authUser) {
        if(!authUser || user.id !== authUser.id) {
            throw new UserException(httpStatus.FORBIDDEN, "you cannot see this user data.");
        }
    }

    validateRequestData(email) {
        if(!email) {
            throw new UserException(httpStatus.BAD_REQUEST, 'User email was not informed.');
        }
    }

    validateUserNotFound(user) {
        if(!user) {
            throw new UserException(httpStatus.BAD_REQUEST, "User was not found.");
        }
    }

    validateAcessTokenData(email, password) {
        if(!email || !password) {
            throw new UserException(httpStatus.UNAUTHORIZED, "Email and Password must be informed.")
        }
    }

    async validatePassword(password, hashPassword) {
        if(!await bcrypt.compare(password, hashPassword)) {
            throw new UserException(httpStatus.UNAUTHORIZED, "Password doesn't match.")
        }
    }
}

export default new UserService();