#pragma once

#include "OperationApi.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example4
				{
					///
					/// <summary> * �����࣬����ʵ�ּӼ������� </summary>
					/// 
					class Operation : public OperationApi
					{
					///	
					///	 <summary> * ��¼����Ľ�� </summary>
					///	 
					private:
						int result;
					public:
						virtual int getResult();
						virtual void setResult(int result);

						virtual void add(int num);
						virtual void substract(int num);
					};

				}
			}
		}
	}
}