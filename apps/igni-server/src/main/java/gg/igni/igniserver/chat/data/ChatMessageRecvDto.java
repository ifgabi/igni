package gg.igni.igniserver.chat.data;

public class ChatMessageRecvDto {
	private String message;

	ChatMessageRecvDto(String message)
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
