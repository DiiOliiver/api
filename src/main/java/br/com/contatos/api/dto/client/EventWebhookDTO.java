package br.com.contatos.api.dto.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventWebhookDTO {

	private String eventId;
	private String subscriptionId;
	private String portalId;
	private String appId;
	private String occurredAt;
	private String subscriptionType;
	private String attemptNumber;
	private String objectId;
	private String changeFlag;
	private String changeSource;
	private String sourceId;

	@Override
	public String toString() {
		return String.format(
			"""
   			{
   				"eventId": "%s",
   				"subscriptionId": "%s",
   				"portalId": "%s",
   				"appId": "%s",
   				"occurredAt": "%s",
   				"subscriptionType": "%s",
   				"attemptNumber": "%s",
   				"objectId": "%s",
   				"changeFlag": "%s",
   				"changeSource": "%s",
   				"sourceId": "%s",
   			}
			""",
			eventId, subscriptionId, portalId, appId, occurredAt,
			subscriptionType, attemptNumber, objectId, changeFlag,
			changeSource, sourceId
		);
	}
}
