#pragma once

#include "DepManager.h"
#include "RequestModel.h"
#include "PreFeeRequestModel.h"

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
					/// <summary> * ʵ�ֲ��ž�����Ԥ֧���÷�������Ķ���  </summary>
					/// 
					class DepManager2 : public DepManager
					{
					public:
						virtual object *handleRequest(RequestModel *request);
					private:
						object *myHandler(RequestModel *request);
					};

				}
			}
		}
	}
}