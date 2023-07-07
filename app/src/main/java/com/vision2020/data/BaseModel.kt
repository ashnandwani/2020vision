import com.vision2020.utils.AppConstant.CODE_ERROR

open class BaseModel {
    /**
     * statusCode : 200
     * message : Registration successful
     */

    var status_code = CODE_ERROR
    var message: String = ""

    constructor() {
        this.status_code = CODE_ERROR
        this.message = ""
    }

    constructor(message: String) {
        this.message = message
    }

    constructor(statusCode: Int, message: String){
        this.message = message
        this.status_code = statusCode
    }
}
