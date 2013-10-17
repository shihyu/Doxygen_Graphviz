#pragma once

#include "Handler.h"
#include "RequestModel.h"
#include "FeeRequestModel.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example5
				{
					///
					/// <summary> * ʵ�ֲ��ž�����۲ͷ�������Ķ���  </summary>
					/// 
					class DepManager : public Handler
					{
					public:
						virtual object *handleRequest(RequestModel *rm);
					private:
						object *handleFeeRequest(RequestModel *rm);
					};

				}
			}
		}
	}
}