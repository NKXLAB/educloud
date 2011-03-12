package com.google.educloud.cloudserver.api.entities.exceptions;

import com.google.educloud.cloudserver.api.entities.EduCloudErrorMessage;

public class EduCloudServerException extends Exception {

	private static final long serialVersionUID = -3037331036352305902L;

	private EduCloudErrorMessage errorMessage;

	public EduCloudServerException(EduCloudErrorMessage errorMessage) {
        super(errorMessage.getText());
        setErrorMessage(errorMessage);
    }

	public EduCloudErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(EduCloudErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

}
