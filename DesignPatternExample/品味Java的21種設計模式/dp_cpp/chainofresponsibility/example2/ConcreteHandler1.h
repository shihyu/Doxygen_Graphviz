#pragma once

#include "Handler.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example2
				{
					///
					/// <summary> * �����ְ����������������� </summary>
					/// 
					class ConcreteHandler1 : public Handler
					{
					public:
						virtual void handleRequest();
					};


				}
			}
		}
	}
}