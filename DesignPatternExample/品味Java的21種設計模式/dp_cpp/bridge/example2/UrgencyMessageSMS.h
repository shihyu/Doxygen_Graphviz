#pragma once

#include "UrgencyMessage.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace bridge
			{
				namespace example2
				{

					class UrgencyMessageSMS : public UrgencyMessage
					{
					public:
						virtual void send(std::string message, std::string toUser);

						virtual object *watch(std::string messageId);
					};

				}
			}
		}
	}
}