#pragma once

#include "Message.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace bridge
			{
				namespace example3
				{
					///
					/// <summary> * �ؼ���Ϣ�ĳ���ӿ� </summary>
					/// 
					class SpecialUrgencyMessage : public Message
					{
					public:
						virtual public void hurry(std::string) = 0;
					};

				}
			}
		}
	}
}