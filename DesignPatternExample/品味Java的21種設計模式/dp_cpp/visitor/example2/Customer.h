#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example2
				{
					///
					/// <summary> * ���ֿͻ��ĸ��� </summary>
					/// 
					class Customer
					{
					///	
					///	 <summary> * �ͻ���� </summary>
					///	 
					private:
						std::string customerId;
					///	
					///	 <summary> * �ͻ����� </summary>
					///	 
						std::string name;

					public:
						virtual std::string getCustomerId();
						virtual void setCustomerId(std::string customerId);
						virtual std::string getName();
						virtual void setName(std::string name);
					///	
					///	 <summary> * �ͻ������������ķ�����ʾ��һ�� </summary>
					///	 
						virtual void serviceRequest() = 0;
					///	
					///	 <summary> * �ͻ��Թ�˾��Ʒ��ƫ�÷�����ʾ��һ�� </summary>
					///	 
						virtual void predilectionAnalyze() = 0;
					///	
					///	 <summary> * �ͻ���ֵ������ʾ��һ�� </summary>
					///	 
						virtual void worthAnalyze() = 0;
					};

				}
			}
		}
	}
}