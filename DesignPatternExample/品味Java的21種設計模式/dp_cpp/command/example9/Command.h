#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example9
				{

					class Command
					{
					public:
						virtual public void execute() = 0;
					///	
					///	 <summary> * ����Ҫ��������� </summary>
					///	 * <param name="s"> Ҫ��������� </param>
					///	 
						virtual public void setStr(std::string) = 0;
					};

				}
			}
		}
	}
}