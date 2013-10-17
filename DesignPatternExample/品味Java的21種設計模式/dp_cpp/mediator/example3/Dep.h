#pragma once

#include "DepUserMediatorImpl.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace mediator
			{
				namespace example3
				{

					///
					/// <summary> * ������ </summary>
					/// 
					class Dep
					{
					///	
					///	 <summary> * �������ű�� </summary>
					///	 
					private:
						std::string depId;
					///	
					///	 <summary> * ������������ </summary>
					///	 
						std::string depName;
					public:
						virtual std::string getDepId();
						virtual void setDepId(std::string depId);
						virtual std::string getDepName();
						virtual void setDepName(std::string depName);
					///	
					///	 <summary> * �������� </summary>
					///	 * <returns> �Ƿ����ɹ� </returns>
					///	 
						virtual bool deleteDep();
					};

				}
			}
		}
	}
}