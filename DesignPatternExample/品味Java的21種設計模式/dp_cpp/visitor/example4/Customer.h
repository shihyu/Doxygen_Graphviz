#pragma once

#include "Visitor.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example4
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
					///	 <summary> * ���ܷ����ߵķ��� </summary>
					///	 * <param name="visitor"> �����߶��� </param>
					///	 
						virtual void accept(Visitor *visitor) = 0;
					};

				}
			}
		}
	}
}