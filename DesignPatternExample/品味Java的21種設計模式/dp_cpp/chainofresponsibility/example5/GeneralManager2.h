#pragma once

#include "GeneralManager.h"
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
					/// <summary> * ʵ���ܾ�����Ԥ֧���÷�������Ķ���  </summary>
					/// 
					class GeneralManager2 : public GeneralManager
					{
					public:
						virtual object *handleRequest(RequestModel *rm);
					private:
						object *myHandler(RequestModel *rm);
					};

				}
			}
		}
	}
}