package gg.igni.igniserver.chat.data;

public class ChatMessageReqDto {
	private String message;

	ChatMessageReqDto(String message)
	{
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
