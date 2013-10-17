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
					/// <summary> * ʵ���ܾ�����۲ͷ�������Ķ���  </summary>
					/// 
					class GeneralManager : public Handler
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